# introduction
    电影知识图谱问答，利用spark，neo4j以及hanlp完成一个简易的电影问答。
    
# requirement
    利用java-driver方式，使用cypher和apoc构建节点和关系，使用spark完成问句分类。
    1.neo4j3.5.3
    2.apoc3.5.0.13
    3.jdk1.8
    4.hanlp1.7.7 下载data(包括dictionary和model，配置hanlp.properties路径)放在resources目录下即可
    5.spark3.0

# quick start
    一.创建节点和关系，这里提供两种方法，分别用cypher和apoc构建。
	    1.cypher构建node与relation(较慢，一个一个语句create)
	        \src\main\java\com\sy\mainclass\GraphCypherBuild.java
	        (1).构建节点
	        createNode(driver);
	        (2).构建关系
            createRelation(driver);
        
        2.apoc批量构建node与relation(建议利用apoc构建，不需要stop neo4j，速度和数据量中等)
            \src\main\java\com\sy\mainclass\GraphApocBuild.java
            (1).构建节点
            createNode(driver);
            (2).构建关系
            createRelation(driver);
        
    二.意图识别为以下几类(样本较少，可以增加更多的数据以提高识别精度)：
        * 0:电影评分
        * 1:电影上映时间
        * 2:电影类型
        * 3:电影简介 (暂时没数据)
        * 4:电影演员列表
        * 5:演员介绍 (暂时没数据)
        * 6:演员参演的某类型的电影作品
        * 7:演员的电影作品
        * 8:演员参演评分大于x的电影
        * 9:演员参演评分小于x的电影
        * 10:演员参演演的电影类型有哪些
        * 11:演员和演员合作电影有哪些
        * 12:演员参数的电影数量
        * 13:演员的出生日期 (暂时没数据)
        \src\main\java\com\sy\mainclass\MovieQA.java(问答部分，问句意图识别和模板匹配，并转为查询语句)
        问答模型的程序在\src\main\java\com\sy\qa中，主要有
            1.利用spark训练意图分类模型
            2.对问句进行意图识别和模板匹配
            3.提取问答语句中的实体，包括人名和电影名
            4.将问句模板和提取的实体转为cypher或apoc语句进行查询
    
    三.电影推荐模块待续...
    
# data
    主要数据在resources中，数据中包含(数据来源http://www.openkg.cn/dataset/douban-movie-kg)
    
    三类实体(节点)：
    实体类型 	数据文件 	数量 	说明
    Movie 	        Movie.csv 	4587 	电影实体
    Person 	        Person.csv 	22937 	人员实体
    Country 	Country.csv 	84 	国家实体
    
    四类关系：
    关系类型 	主语实体类型 	宾语实体类型 	数据文件 	数量 	说明
    ACTOR_OF 	    Movie 	    Person 	actor.csv 	35257 	电影的主演
    COMPOSER_OF 	    Movie 	    Person 	composer.csv 	8345 	电影的编剧
    DIRECTOR_OF 	    Movie 	    Person 	director.csv 	5015 	电影的导演
    DISTRICT_OF 	    Movie 	    Country 	district.csv 	6227 	电影的制片国家/地区

# flowchart

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/flowchart.png)

# result

1.关系图

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/movie_graph.png)

2.问答

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/reponse_result.png)

# references

１）http://www.openkg.cn/dataset/douban-movie-kg

２）https://www.zmonster.me/2019/04/30/neo4j-introduction.html

