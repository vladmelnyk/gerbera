package sd.fomin.gerbera.constant;

public interface ErrorMessages {
    String INPUT_TRANSACTION_EMPTY = "Transaction hash must not be null or empty";
    String INPUT_TRANSACTION_NOT_64_HEX = "Transaction hash must be 64 digit hex";
    String INPUT_INDEX_NEGATIVE = "Previous transaction output index must not be negative";
    String INPUT_LOCK_EMPTY = "Locking script must not be null or empty";
    String INPUT_LOCK_NOT_HEX = "Locking script must be in hex";
    String INPUT_AMOUNT_NOT_POSITIVE = "Amount of satoshi must be a positive value";
    String INPUT_LOCK_WRONG_FORMAT = "Provided locking script is not P2PKH or P2SH [%s]";
    String INPUT_WRONG_PKH_SIZE = "Incorrect PKH size [%s]";
    String INPUT_WRONG_RS_SIZE = "Incorrect redeemScript size [%s]";
    String INPUT_WIF_EMPTY = "WIF must not be null or empty";
    String INPUT_WIF_NOT_BASE_58 = "WIF must contain only base58 characters";
    String OUTPUT_ADDRESS_EMPTY = "Address must not be null or empty";
    String OUTPUT_ADDRESS_NOT_BASE_58 = "Address must contain only base58 characters";
    String OUTPUT_ADDRESS_WRONG_PREFIX = "Only addresses starting with %s (P2PKH) or %s (P2SH) supported.";
    String OUTPUT_AMOUNT_NOT_POSITIVE = "Amount of satoshi must be a positive value";
    String OUTPUT_DATA_IS_EMPTY = "Data must not be empty";
    String OUTPUT_DATA_IS_NOT_HEX = "Data must hex string";
    String OUTPUT_DATA_BYTES_HUGE = "Data size in bytes (%d) is larger than max allowed (%d).";
    String OUTPUT_TYPE_NULL = "Output type must not be null";
    String SPK_UNSUPPORTED_PRODUCER = "Unsupported producer for [mainnet: %b, prefix: %d]";
    String FEE_NEGATIVE = "Fee must not be less than zero";
    String BASE58_WRONG_CS = "Wrong Base58 checksum";
    String OP_SIZE_NOT_IMPLEMENTED = "Only one byte op size is supported. To be implemented";
    String UINT_NOT_REPRESENTABLE = "The number is more than 255 and can't be represented as one byte";
}
