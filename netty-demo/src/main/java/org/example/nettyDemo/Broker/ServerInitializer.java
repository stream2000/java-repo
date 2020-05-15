package org.example.nettyDemo.Broker;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufDecoder;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufEncoder;

import java.util.HashMap;
import java.util.Map;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
@Override
protected void initChannel(SocketChannel ch) throws Exception {
    Map<Byte, GeneratedMessageV3> decodeMap = new HashMap<>();
    decodeMap.put((byte)0x01, ProtobufOuter.UserMessage.getDefaultInstance());
    ch.pipeline().addLast(new CustomProtobufDecoder(decodeMap));
    Map<Class, Byte>encodeMap = new HashMap<>();
    encodeMap.put(ProtobufOuter.Response.class,(byte)0x01);
    ch.pipeline().addLast(new CustomProtobufEncoder(encodeMap));
    ch.pipeline().addLast(new ServerHandler());
}
}
