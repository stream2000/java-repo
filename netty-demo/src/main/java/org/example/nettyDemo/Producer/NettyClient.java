package org.example.nettyDemo.Producer;

import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;


public class NettyClient {
public static void main(String[] args) throws Exception {
    new NettyClient("127.0.0.1", 9999).run();
}

private final String host;
private final int port;

public NettyClient(String host, int port) {
    this.host = host;
    this.port = port;
}

public void run() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        Channel channel = bootstrap.connect(host, port).sync().channel();
        System.out.println(channel.remoteAddress());
        for (int i = 0; i < 10; i++) {
            System.out.println("sent the message with id "+i);
            ProtobufOuter.UserMessage letter = ProtobufOuter.UserMessage.newBuilder()
                    .setLetterId(i)
                    .setType(ProtobufOuter.UserMessage.payload_type.Text)
                    .setData(ByteString.copyFromUtf8("hello world"))
                    .build();
            channel.writeAndFlush(letter);
        }
        channel.closeFuture().sync();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        group.shutdownGracefully();
    }
}
}

