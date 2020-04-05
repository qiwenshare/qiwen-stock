# 股票分析系统

#### 介绍

1、股票数据的采集，通过java去互联网爬取基础的数据，对数据进一步处理后，进行保存
主要是对近十年的一千三百多支股票的日数据进行采集，然后获取需要的数据，总数据量达到500多万条，首次全量采集，第二次增量采集，系统会自动识别，以减少时间消耗

2、对数据做进一步的分析，包括各种指标的计算和历史数据的回测

3、分析过程中，后台实时向前台推送进度数据

4、在前台进行展示，以及各种分析结果，k线图等

5、系统权限管理，普通用户只有查看权限，管理员用户有各种操作权限


#### 软件架构
软件架构说明
后台：springboot，mybatis, hibernate, spring data jpa，shiro, aop

前台：Ajax, echart, element-ui, vue, jquery

数据推送：websocket

数据库：mysql

开发工具：idea

## 安装教程

1. 拉取代码
2. 本地创建数据库，名为stock，将application.properties中连接数据库的密码替换为自己本地的
3. 点击根目录下install.bat进行编译
4. 编译完成之后会生成release发布包，进去点击startStock.bat启动

## 部分功能展示
![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/155558_c0cec849_947714.png "屏幕截图.png")

因为目前还在开发，所以暂时就将这些管理端操作放到这里吧，这里主要是爬取上海证券交易所的数据，主要原因是他们的网站比较稳定

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/155812_f6d10a18_947714.png "屏幕截图.png")

下面分别是分时线，日线，周线，还有月线，通过echarts工具进行渲染的

分时线

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/160017_3803163a_947714.png "屏幕截图.png")

日线

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/160323_3f930c65_947714.png "屏幕截图.png")

周线

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/160419_cfcd225f_947714.png "屏幕截图.png")

月线

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/160626_c9459226_947714.png "屏幕截图.png")

还有最最重要的，数据回测和数据预测功能，主要是根据算法来分析和预测

![输入图片说明](https://images.gitee.com/uploads/images/2020/0405/160856_af237bf6_947714.png "屏幕截图.png")


## 联系我
如果对该项目感兴趣，或者有各种问题可扫描加入QQ群

![输入图片说明](https://images.gitee.com/uploads/images/2020/0404/230620_c45ef962_947714.png "屏幕截图.png")



#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
