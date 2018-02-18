package sd.fomin.gerbera.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ByteBufferTest {

    @Test
    public void testEmpty() {
        ByteBuffer buffer = new ByteBuffer();

        assertThat(buffer.size()).isZero();
        assertThat(buffer.bytes()).isEmpty();
        assertThat(buffer.bytesReversed()).isEmpty();
    }

    @Test
    public void testAppend() {
        ByteBuffer buffer = new ByteBuffer();
        buffer.append((byte) 0);
        buffer.append(new byte[] { 1, 2 });
        buffer.append(new byte[] { 3, 4, 5 });

        assertThat(buffer.size()).isEqualTo(6);
        assertThat(buffer.bytes()).containsExactly(0, 1, 2, 3, 4, 5);
        assertThat(buffer.bytesReversed()).containsExactly(5, 4, 3, 2, 1, 0);
    }

    @Test
    public void testPutFirst() {
        ByteBuffer buffer = new ByteBuffer();
        buffer.putFirst((byte) 0);
        buffer.putFirst(new byte[] { 1, 2 });
        buffer.putFirst(new byte[] { 3, 4, 5 });

        assertThat(buffer.size()).isEqualTo(6);
        assertThat(buffer.bytes()).containsExactly(3, 4, 5, 1, 2, 0);
        assertThat(buffer.bytesReversed()).containsExactly(0, 2, 1, 5, 4, 3);
    }
}
