package org.fomky.ratpack.core.controller;

import org.fomky.ratpack.core.utils.ChainUtil;
import org.springframework.beans.factory.annotation.Value;
import ratpack.exec.Promise;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.handling.RequestLogger;
import ratpack.session.Session;
import ratpack.session.SessionKey;

/**
 * @author Created by Fomky on 2017/3/1811:41.
 */
public abstract class UserControllers implements CoreHandle {

    @Value("${server.api.prefix.users}")
    private String prefix;

    @Override
    public void execute(Chain chain) throws Exception {
        //Add Error 处理
        ChainUtil.addAllSupport(chain)
                //Add 访问日志处理
                .all(RequestLogger.of(outcome -> {
                    System.out.println("访问日志 ： " + outcome.getRequest().getUri());
                }))
                //Add users 处理函数
                .prefix(prefix, this::users);
    }

    protected abstract void users(Chain chain);

    /**
     * 获取 Session 中Userid 如果没有， 返回客户端错误信息
     * @return
     */
    protected Promise<String> needUser(Context context){
        return session(context).require(SessionKey.of(SESSION_USERID, String.class));
    }

    /**
     * 获取Session 对象
     * @param context
     * @return
     */
    protected Session session(Context context){
        return context.get(Session.class);
    }



}
