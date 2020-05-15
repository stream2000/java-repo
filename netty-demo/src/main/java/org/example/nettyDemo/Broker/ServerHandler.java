package org.example.nettyDemo.Broker;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.example.nettyDemo.Common.ProtoRouter.Handler;
import org.example.nettyDemo.Common.ProtoRouter.ProtoRouter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
private ProtoRouter router;
ServerHandler(ProtoRouter router){
   this.router = router;
}
@Override
public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
    Channel incoming = ctx.channel();
//    for (Channel channel : channels) {
//        channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入");
//    }
    System.out.println("[SERVER] - " + incoming.remoteAddress() + " added");
    channels.add(ctx.channel());
}

@Override
public  void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
    Handler handler = router.getHandler(msg);
    if(handler == null){
        // TODO more check. may be trhe
        return;
    }
    handler.handle(channelHandlerContext, (GeneratedMessageV3) msg);
}
}
