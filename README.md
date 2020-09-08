# douban-movie-kg
    存储豆瓣电影知识图谱，展示导演、编剧、演员与电影的关系。
# introduction
    利用java-driver方式，使用cypher和apoc构建节点和关系。
    neo4j3.5.3
    apoc3.5.0.13
    jdk8
    hanlp1.7.7 下载data(包括dictionary和model，配置hanlp.properties路径)放在resources目录下即可

# quick start

	一.cypher构建node与relation 
	    \src\main\java\com\sy\mainclass\GraphCypherBuild.java
	    1.构建节点
	    createNode(driver);
	    2.构建关系
        createRelation(driver);
        
    二.apoc批量构建node与relation
        \src\main\java\com\sy\mainclass\GraphApocBuild.java
        1.构建节点
        createNode(driver);
        2.构建关系
        createRelation(driver);
        
    三.\src\main\java\com\sy\mainclass\MovieQA.java
        1.提取问答语句中的实体，包括人名和电影名
    
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

# cases

1)节点

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/person.jpg)

2)关系

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/actor.jpg)

# references

１）http://www.openkg.cn/dataset/douban-movie-kg

２）https://www.zmonster.me/2019/04/30/neo4j-introduction.html

