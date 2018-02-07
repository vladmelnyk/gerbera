package sd.fomin.gerbera.transaction;

import sd.fomin.gerbera.constant.OpCodes;
import sd.fomin.gerbera.constant.SigHashType;
import sd.fomin.gerbera.types.OpSize;
import sd.fomin.gerbera.util.ByteBuffer;
import sd.fomin.gerbera.crypto.PrivateKey;
import sd.fomin.gerbera.types.UInt;
import sd.fomin.gerbera.types.VarInt;
import sd.fomin.gerbera.util.HashUtils;
import sd.fomin.gerbera.util.HexUtils;

import static sd.fomin.gerbera.util.ValidationUtils.isBase58;
import static sd.fomin.gerbera.util.ValidationUtils.isEmpty;
import static sd.fomin.gerbera.util.ValidationUtils.isHexString;
import static sd.fomin.gerbera.util.ValidationUtils.isTransactionId;

class Input {

    private static final UInt SEQUENCE = UInt.of(0xFFFFFFFF);

    private final String transaction;
    private final int index;
    private final String lock;
    private final long satoshi;
    private final String wif;
    private final PrivateKey privateKey;

    Input(boolean mainNet, String transaction, int index, String lock, long satoshi, String wif) {
        validateInputData(transaction, index, lock, satoshi, wif);

        this.transaction = transaction;
        this.index = index;
        this.lock = lock;
        this.satoshi = satoshi;
        this.wif = wif;
        this.privateKey = PrivateKey.ofWif(mainNet, wif);
    }

    void fillTransaction(byte[] sigHash, Transaction transaction) {
        boolean segWit = isSegWit();

        transaction.addHeader(segWit ? "   Input (Segwit)" : "   Input");

        byte[] unlocking = segWit ? createUnlockSegwit() : createUnlockRegular(sigHash);
        transaction.addData("      Transaction out", HexUtils.asString(getTransactionHashBytesLitEnd()));
        transaction.addData("      Tout index", UInt.of(index).toString());
        transaction.addData("      Unlock length", HexUtils.asString(VarInt.of(unlocking.length).asLitEndBytes()));
        transaction.addData("      Unlock", HexUtils.asString(unlocking));
        transaction.addData("      Sequence", SEQUENCE.toString());
    }

    private byte[] createUnlockRegular(byte[] sigHash) {
        ByteBuffer result = new ByteBuffer();

        result.append(privateKey.sign(sigHash));
        result.append(SigHashType.ALL.asByte());

        result.putFirst(OpSize.ofInt(result.size()).getSize());

        byte[] publicKey = privateKey.getPublicKey();
        result.append(OpSize.ofInt(publicKey.length).getSize());
        result.append(publicKey);

        return result.bytes();
    }

    private byte[] createUnlockSegwit() {
        ByteBuffer result = new ByteBuffer();

        result.append(OpCodes.FALSE);
        result.append((byte) 0x14); //ripemd160 size
        result.append(HashUtils.ripemd160(HashUtils.sha256(privateKey.getPublicKey())));
        result.putFirst(OpSize.ofInt(result.size()).getSize()); //PUSH DATA

        return result.bytes();
    }

    byte[] getWitness(byte[] sigHash) {
        ByteBuffer result = new ByteBuffer();
        if (isSegWit()) {
            result.append((byte) 0x02);

            ByteBuffer sign = new ByteBuffer(privateKey.sign(sigHash));
            sign.append(SigHashType.ALL.asByte());
            result.append(OpSize.ofInt(sign.size()).getSize());
            result.append(sign.bytes());

            byte[] pubKey = privateKey.getPublicKey();
            result.append(OpSize.ofInt(pubKey.length).getSize());
            result.append(pubKey);
        } else {
            result.append((byte) 0x00);
        }

        return result.bytes();
    }

    byte[] getTransactionHashBytesLitEnd() {
        return new ByteBuffer(HexUtils.asBytes(transaction)).bytesReversed();
    }

    long getSatoshi() {
        return satoshi;
    }

    boolean isSegWit() {
        return LockScriptType.forLock(lock).isSegWit();
    }

    int getIndex() {
        return index;
    }

    String getLock() {
        return lock;
    }

    PrivateKey getPrivateKey() {
        return privateKey;
    }

    UInt getSequence() {
        return SEQUENCE;
    }

    @Override
    public String toString() {
        return transaction +
                " " + index +
                " " + lock +
                " " + satoshi +
                " " + wif;
    }

    private void validateInputData(String transaction, int index, String lock, long satoshi, String wif) {
        validateTransactionId(transaction);
        validateOutputIndex(index);
        validateLockingScript(lock);
        validateAmount(satoshi);
        validateWif(wif);
    }

    private void validateTransactionId(String transaction) {
        if (isEmpty(transaction)) {
            throw new IllegalArgumentException("Transaction hash must not be null or empty");
        }
        if (!isTransactionId(transaction)) {
            throw new IllegalArgumentException("Transaction hash must be 64 digit hex");
        }
    }

    private void validateOutputIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Previous transaction output index must be a positive value");
        }
    }

    private void validateLockingScript(String lock) {
        if (isEmpty(lock)) {
            throw new IllegalArgumentException("Locking script must not be null or empty");
        }
        if (!isHexString(lock)) {
            throw new IllegalArgumentException("Locking script must be in hex");
        }
        LockScriptType lockScriptType = LockScriptType.forLock(lock);
        if (LockScriptType.P2PKH.equals(lockScriptType)) {
            byte[] lockBytes = HexUtils.asBytes(lock);
            OpSize pubKeyHashSize = OpSize.ofByte(lockBytes[2]);
            if (pubKeyHashSize.getSize() != lockBytes.length - 5) {
                throw new IllegalArgumentException("Incorrect PKH size. " +
                        "Expected: " + pubKeyHashSize.getSize() +
                        ". [" + lock + "]");
            }
        } else if (LockScriptType.P2SH.equals(lockScriptType)) {
            byte[] lockBytes = HexUtils.asBytes(lock);
            OpSize pubKeyHashSize = OpSize.ofByte(lockBytes[1]);
            if (pubKeyHashSize.getSize() != lockBytes.length - 3) {
                throw new IllegalArgumentException("Incorrect redeemScript size. " +
                        "Expected: " + pubKeyHashSize.getSize() +
                        ". [" + lock + "]");
            }
        } else {
            throw new IllegalArgumentException("Provided locking script is not P2PKH or P2SH [" + lock + "]");
        }
    }

    private void validateAmount(long satoshi) {
        if (satoshi <= 0) {
            throw new IllegalArgumentException("Amount of satoshi must be a positive value");
        }
    }

    private void validateWif(String wif) {
        if (isEmpty(wif)) {
            throw new IllegalArgumentException("WIF must not be null or empty");
        }
        if (!isBase58(wif)) {
            throw new IllegalArgumentException("WIF must contain only base58 characters");
        }
    }
}
