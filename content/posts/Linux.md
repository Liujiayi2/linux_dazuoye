---
title: Linux
date: 2023-01-02T13:11:07+08:00
lastmod: 2023-01-02T13:11:07+08:00
author: Jiayi Liu
# avatar: /img/author.jpg
# authorlink: https://author.site
cover: /img/default2.jpg
# images:
#   - /img/cover.jpg
categories:
  - category1
tags:
  - tag1
  - tag2
# nolastmod: true
draft: true
---
## 安装配置Git
---
**1.安装Git**
>`sudo yum install git`
---
**2.配置Git**

（1）配置用户名和邮箱：

>`git config --global user.name "Liujiayi2"`

>`git config --global user.email "example@example.com"`

>`git config --list`

（2）获取ssh密钥：

>`ssh-keygen -t rsa -C "example@example.com"`

>`cat /root/.ssh/id_rsa.pub`

（3）成功建立和Github的联系：

![](/img/图片1.png)

>`ssh git@github.com`

（4）关联到远程仓库：

>`git remote add origin git@github.com:Liujiayi2/linux_dazuoye.git`

>`git push -u origin "main"`

>Ps:如果`git push -u origin "main"`运行不成功，可以试一下代码：`git remote set-url origin git@github.com:your_username/xxx.git`

---
使用docker和socket技术使客户端和服务端之间的信息传递
---

（1）安装docker：
>`sudo yum install docker`

（2）在远程仓库下载tomcat镜像：

>`docker pull tomcat`

（3）运行镜像创建两个容器，分别名为fwd（服务端）和khd（客户端），再设置端口映射，fwd的端口为2002，khd的端口为2004：

>`docker run -itd --name fwd -p 2002:2002 tomcat`

>`docker run -itd --name khd -p 2004:2002 tomcat`

（4）查看fwd的IP地址：

>`docker inspect --format='{{.NetworkSettings.IPAddress}}' fwd`

![](/img/图片2.png)

（5）创建一个服务端类和一个客户端类:

>`vim SocketService.java`

>服务端代码如下：
```html
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class SocketService{
        public static void main(String[] args) throws IOException{
                SocketService socketService = new SocketService();
                socketService.oneServer();}
        public void oneServer(){
                try{
                        ServerSocket server = null;
                        try{
                                server = new ServerSocket(2002);
                                System.out.println("Service enable Success");}
                        catch(Exception e){System.out.println("No Listen:" + e);}                        Socket socket = null;
                        try{socket = server.accept();}
                        catch(Exception e){System.out.println("Error." + e);}
                        String line;
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("Client:" + in.readLine());
                        line = br.readLine();
                        while(!line.equals("end")){
                                writer.println(line);
                                writer.flush();
                                System.out.println("Service:" + line);
                                System.out.println("Client:" + in.readLine());
                                line = br.readLine();}
                        writer.close();
                        in.close();
                        socket.close();
                        server.close();}
                catch(Exception e){System.out.println("Error." + e);}}}
```

>`git add SocketService.java`

>`git commit -m "新建一个服务端"`

>`git push -u origin "main"`

>`vim SocketClient.java`

>客户端代码如下：
```html
import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
public class SocketClient{
        public static void main(String[] args) throws IOException{
                try{
                        Socket socket = new Socket("172.17.0.2",2002);
                        System.out.println("Client enable Success");
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String readline;
                        readline = br.readLine();
                        while(!readline.equals("end")){
                                writer.println(readline);
                                writer.flush();
                                System.out.println("Client:" + readline);
                                System.out.println("Service:" + in.readLine());
                                readline = br.readLine();}
                        writer.close();
                        in.close();
                        socket.close();}
                catch(Exception e){System.out.println("can not listen to" + e);}}}
```

>`git add SocketClient.java`

>`git commit -m "新建一个客户端"`

>`git push -u origin "main"`

![](/img/图片3.png)

（6）将服务器端和客户端代码分别拷贝到对应容器的镜像里：

>`docker cp SocketService.java fwd:/`

>`docker cp SocketClient.java khd:/`

（7）进入服务器的容器和客户端的容器，然后启动服务器端和客户端程序，开始通信：

![](/img/图片4.png)

![](/img/图片5.png)

---
安装hugo并创建静态网站
---

**1.安装并配置Hugo**

（1）下载hugo包：

>`wget https://github.com/gohugoio/hugo/releases/download/v0.80.0/hugo_0.80.0_Linux-64bit.deb`

（2）安装hugo：

>`dnf install dpkg`

>`dpkg -i hugo_0.80.0_Linux-64bit.deb`

（3）新建站点blog：

>`hugo new site blog`

（4）下载一个主题：

>`git submodule add https://github.com/vimux/mainroad.git themes/mainroad`

（5）配置config.toml

>`vim config.toml`
```

```
---
**2.使用Markdown语法编写网站内容**

（1）新建一篇文章：

>`hugo new posts/Linux_dazuoye.md`

（2）编写文章：

![](/img/图片6.png)

（3）在本地使用hugo命令在根目录生成public文件夹：

![](/img/图片7.png)
---
**3.运用docker技术部署静态网站到web服务器**

（1）创建一个容器web：

（2）
---
