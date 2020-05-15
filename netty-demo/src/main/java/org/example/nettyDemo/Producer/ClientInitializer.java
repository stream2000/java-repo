package org.example.nettyDemo.Producer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.example.nettyDemo.Common.ProtoRouter.ProtoRouter;
import org.example.nettyDemo.Common.Proto.RouterInitializer;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufDecoder;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

@Override
public void initChannel(SocketChannel ch) throws Exception {
    // initialize decode mapping
    ProtoRouter router = RouterInitializer.initialize();
    ch.pipeline().addLast(new CustomProtobufDecoder(router));
    ch.pipeline().addLast(new CustomProtobufEncoder(router));
    ch.pipeline().addLast(new ClientHandler());
    System.out.println("initialize the client");
}
}

