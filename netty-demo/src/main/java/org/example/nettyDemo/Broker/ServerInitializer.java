package org.example.nettyDemo.Broker;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.example.nettyDemo.Broker.Handler.UserMessageHandler;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;
import org.example.nettyDemo.Common.ProtoRouter.ProtoRouter;
import org.example.nettyDemo.Common.Proto.RouterInitializer;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufDecoder;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
@Override
protected void initChannel(SocketChannel ch) throws Exception {
    ProtoRouter router = RouterInitializer.initialize();
    ch.pipeline().addLast(new CustomProtobufEncoder(router));
    ch.pipeline().addLast(new CustomProtobufDecoder(router));
    router.registerHandler(ProtobufOuter.UserSendRequest.getDefaultInstance(), new UserMessageHandler());
    ch.pipeline().addLast(new ServerHandler(router));
}
}
