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

前台：Ajax, echart, layui, vue, jquery

数据推送：websocket

数据库：mysql

开发工具：idea

#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
