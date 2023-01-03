---
title: Linux
date: 2023-01-03T17:12:48+08:00
lastmod: 2023-01-03T17:12:48+08:00
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

>`git config --global user.email "2844545327@qq.com"`

>`git config --list`

（2）获取ssh密钥：

>`ssh-keygen -t rsa -C "2844545327@qq.com"`

>`cat /root/.ssh/id_rsa.pub`

（3）成功建立和Github的联系：

![](/img/图片1.png)

>`ssh git@github.com`

（4）关联到远程仓库：

>`git remote add origin git@github.com:Liujiayi2/linux_dazuoye.git`

>`git push -u origin "main"`

>Ps:如果`git push -u origin "main"`运行不成功，可以先运行代码`git remote set-url origin git@github.com:Liujiayi2/linux_dazuoye.git`，然后再重新运行`git push -u origin "main"`。

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

服务端代码如下：
```java
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
                        catch(Exception e){System.out.println("No Listen:" + e);}
                        Socket socket = null;
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

客户端代码如下：
```java
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

config.toml配置文件如下：
```toml
baseurl = "http://192.168.92.132/"
languageCode = "en"
defaultContentLanguage = "en"
title = "一家之煮的博客"
theme = "dream"

# copyright = ""

# googleAnalytics = ""

# disqusShortname = ""

# enableRobotsTXT = true

[params]
  background = "white"
  backgroundImage = "/me/background.jpg"
  linkColor = "seagreen"

  # dark mode
  defaultDark = true
  backgroundDark = "black"
  backgroundImageDark = "/me/background-dark.jpg"
  darkLinkColor = "darkseagreen"
  darkNav = true
  dark404 = true

  author = "Jiayi Liu"
  # description = ""
  avatar = "/img/avatar.jpeg"
  headerTitle = "一家之煮"
  motto = "一以贯之的努力，不得懈怠的人生。"
  # maxTags = 5
  # categoriesLimitInHeader = 6 # deprecated
  # headerBottomText = "" # deprecated

  # footerBottomText = ""

  # rss = true

  # utterancesRepo = ""

  # valine = true
  # LEANCLOUD_APP_ID = ""
  # LEANCLOUD_APP_KEY = ""
  # VALINE_LANGUAGE = ""

  email = "2844545327@qq.com"
  github = "Liujiayi2"

  siteStartYear = 2022

  # favicon = "/favicon.ico"

  # highlightjs = true
  # highlightjsCDN = "https://cdn.jsdelivr.net/gh/highlightjs/cdn-release/build/highlight.min.js"
  # highlightjsExtraLanguages = ["ocaml"]
  # highlightjsTheme = "gruvbox-light"
  # highlightjsThemeDark = "gruvbox-dark"

  # search
  # enableSearch = true

  # options
  # showSummaryCoverInPost = true
  # hasTwitterEmbed = true
  # reversePostAndAside = true
  # shareInAside = true
  # fixedNav = true
  # collapsibleTags = true
  # collapseBySummary = true
  # disableFlip = true
  # hideBackSocialLinks = true

  # [params.advanced]
  #   customCSS = ["css/custom.css"]
  #   customJSBefore = []
  #   customJS = []

  # [params.experimental]
  #   jsDate = true
  #   jsDateFormat = "2023年01月02日"
```

---
**2.使用Markdown语法编写网站内容**

（1）新建一篇文章：

>`hugo new posts/Linux.md`

（2）编写文章：

![](/img/图片6.png)

（3）在本地使用hugo命令在根目录生成public文件夹：

>`hugo -D`

---
**3.运用docker技术部署静态网站到web服务器**

（1）下载并解压nginx包：

>`wget http://nginx.org/download/nginx-1.23.3.tar.gz`

>`tar -xzvf nginx-1.23.3.tar.gz`

（2）配置基本信息：

>`./configure --prefix=/usr/local/nginx-1.23.3 --with-http_ssl_module --with-http_stub_status_module`

（3）编译安装：

>`make & make install`

（4）进入到sbin目录，启动nginx：

>`cd /usr/local/nginx-1.23.3/sbin`

>`./nginx`

（5）访问IP:80：

>`192.168.92.132:80`

![](/img/图片7.png)

（6）将public文件夹里的全部文件复制到/usr/local/nginx-1.23.3/html中：

>`cp -r public /usr/local/nginx-1.23.3/html`

（7）修改conf目录下的nginx.conf文件，并且重载nginx：

修改的部分如下：
```conf
server {
        listen       888;
        server_name  localhost;
        location / {
            root   html/public;
            index  index.html index.xml;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
}
```

>`../sbin/nginx -s reload`

（8）在浏览器上访问nginx服务器可以看到网站内容：

![](/img/图片8.png)

（9）点击文章也能看到文章内容：

![](/img/图片9.png)

（10）部署到github上，公网能访问：

![](/img/图片10.png)

>`git remote add origin git@github.com:Liujiayi2/Liujiayi2.github.io.git`

![](/img/图片11.png)
