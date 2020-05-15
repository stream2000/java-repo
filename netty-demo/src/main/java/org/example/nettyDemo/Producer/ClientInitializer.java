package org.example.nettyDemo.Producer;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.example.nettyDemo.Common.*;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufDecoder;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufEncoder;

import java.util.HashMap;
import java.util.Map;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

@Override
public void initChannel(SocketChannel ch) throws Exception {
    // initialize decode mapping
    Map<Byte, GeneratedMessageV3>decodeMap = new HashMap<>();
    decodeMap.put((byte)0x01, ProtobufOuter.Response.getDefaultInstance());
    ch.pipeline().addLast(new CustomProtobufDecoder(decodeMap));
    // initialize encode mapping
    Map<Class, Byte>encodeMap = new HashMap<>();
    encodeMap.put(ProtobufOuter.UserMessage.class,(byte)0x01);
    ch.pipeline().addLast(new CustomProtobufEncoder(encodeMap));

    ch.pipeline().addLast(new ClientHandler());
    System.out.println("initialize the client");
}
}

