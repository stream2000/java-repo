package org.example.nettyDemo.Broker.Handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;
import org.example.nettyDemo.Common.ProtoRouter.Handler;

import java.util.concurrent.*;

public class UserMessageHandler implements Handler {
private final static ExecutorService workerThreadService = newBlockingExecutorsUseCallerRun(Runtime.getRuntime().availableProcessors() * 2);

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

@Override
public void handle(ChannelHandlerContext ctx, GeneratedMessageV3 msg) throws Exception {
    ProtobufOuter.UserSendRequest letter = (ProtobufOuter.UserSendRequest) msg;
    workerThreadService.execute(() -> {
        System.out.println("received a letter:\n " + letter);
        System.out.println("then make persistence and replication, which may take some time");
        // use sleep to mock a blocking call
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProtobufOuter.UserSendResponse resp = ProtobufOuter.UserSendResponse.newBuilder().setLetterId(letter.getLetterId()).build();
        // netty writeAndFlush 已经做了处理，会主动切换到原生的io线程中完成剩下逻辑
        // something like async write?
        ctx.writeAndFlush(resp);
    });
}
}
