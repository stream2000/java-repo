package org.example.nettyDemo.Common.ProtobufCodec;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Map;

/**
 * 参考ProtobufVarint32LengthFieldPrepender 和 ProtobufEncoder
 */
@Sharable
public class CustomProtobufEncoder extends MessageToByteEncoder<MessageLite> {

private static Map<Class, Byte> messageMap;

public CustomProtobufEncoder(Map<Class, Byte> map) {
    messageMap = map;
}

@Override
protected void encode(
        ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {

    byte[] body = msg.toByteArray();
    byte[] header = encodeHeader(msg, (short) body.length);
    out.writeBytes(header);
    out.writeBytes(body);
}

private byte[] encodeHeader(MessageLite msg, short bodyLength) {
    Byte messageType= messageMap.get(msg.getClass());
    // TODO check when type = 0
    byte[] header = new byte[4];
    header[0] = (byte) (bodyLength & 0xff);
    header[1] = (byte) ((bodyLength >> 8) & 0xff);
    header[2] = 0; // 保留字段
    header[3] = messageType;
    return header;
}
}
