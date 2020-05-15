package org.example.nettyDemo.Broker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;

import java.util.concurrent.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {
private final static ExecutorService workerThreadService = newBlockingExecutorsUseCallerRun(Runtime.getRuntime().availableProcessors() * 2);
public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

@Override
public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
    Channel incoming = ctx.channel();
//    for (Channel channel : channels) {
//        channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入");
//    }
    System.out.println("[SERVER] - " + incoming.remoteAddress() + " 加入");
    channels.add(ctx.channel());
}

@Override
public  void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
    ProtobufOuter.UserMessage letter = (ProtobufOuter.UserMessage)msg;
    workerThreadService.execute(() -> {
        System.out.println("received a letter:\n " + letter);
        System.out.println("then make persistence and replication, which may take some time");
        // use sleep to mock a blocking call
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProtobufOuter.Response resp = ProtobufOuter.Response.newBuilder().setLetterId(letter.getLetterId()).build();
        // netty writeAndFlush 已经做了处理，会主动切换到原生的io线程中完成剩下逻辑
        // something like async write?
        channelHandlerContext.writeAndFlush(resp);
    });
}

public static ExecutorService newBlockingExecutorsUseCallerRun(int size) {
    return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
}
}
