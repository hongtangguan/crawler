package com.juban.task.boss;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;

public class ProxyProviderTest implements ProxyProvider {


    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {



    }

    @Override
    public Proxy getProxy(Task task) {



        return null;
    }
}
