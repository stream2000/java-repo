package org.example.nettyDemo.Broker;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.example.nettyDemo.Broker.Handler.UserMessageHandler;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;
import org.example.nettyDemo.Common.Proto.RouterInitializer;
import org.example.nettyDemo.Common.ProtoRouter.Handler;
import org.example.nettyDemo.Common.ProtoRouter.ProtoRouter;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufDecoder;
import org.example.nettyDemo.Common.ProtobufCodec.CustomProtobufEncoder;

import java.net.InetSocketAddress;

public class Server {

private final int port;
public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
private ProtoRouter router;

public Server(int port) {
    this.port = port;
    router = RouterInitializer.initialize();
}

public void run() throws Exception {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(this.new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        System.out.println("Started Server");
        ChannelFuture future = b.bind().sync();
        future.channel().closeFuture().sync();
    } finally {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

        System.out.println("close server");
    }
}

class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        // hook: add this channel to corresponding channel group
        Channel incoming = ctx.channel();
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " added");
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Handler handler = router.getHandler(msg);
        if (handler == null) {
            // TODO more check
            return;
        }
        handler.handle(channelHandlerContext, (GeneratedMessageV3) msg);
    }
}

class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new CustomProtobufEncoder(router));
        ch.pipeline().addLast(new CustomProtobufDecoder(router));
        router.registerHandler(ProtobufOuter.UserSendRequest.getDefaultInstance(), new UserMessageHandler());
        ch.pipeline().addLast(new ServerHandler());
    }
}

public static void main(String[] args) throws Exception {
    int port;
    if (args.length > 0) {
        port = Integer.parseInt(args[0]);
    } else {
        port = 9999;
    }
    new Server(port).run();
}
}
