# douban-movie-kg
    存储豆瓣电影的知识图谱，展示导演、编剧、演员与电影的关系。
# introduction
    利用java-driver方式，使用cypher构建节点和关系。
    neo4j3.5.3
    jdk8

# quick start

	一.\src\main\java\com\sy\mainclass\GraphBuild.java：
	    1.创建节点
	    createNode(driver);
	    2.创建关系
        createRelation(driver);
        3.一些简单的搜索
        search(driver);
        
    二.\src\main\java\com\sy\mainclass\MovieQA.java
    
# data

    主要数据在resources中，数据中包含(数据来源http://www.openkg.cn/dataset/douban-movie-kg)
    
    三类实体(节点)：
    实体类型 	数据文件 	数量 	说明
    Movie 	        Movie.csv 	4587 	电影实体
    Person 	        Person.csv 	22937 	人员实体
    Country 	Country.csv 	84 	国家实体
    
    四类关系：
    关系类型 	主语实体类型 	宾语实体类型 	数据文件 	数量 	说明
    actor 	            Movie 	    Person 	actor.csv 	35257 	电影的主演
    composer 	    Movie 	    Person 	composer.csv 	8345 	电影的编剧
    director 	    Movie 	    Person 	director.csv 	5015 	电影的导演
    district 	    Movie 	    Country 	district.csv 	6227 	电影的制片国家/地区

# cases

1)节点

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/person.jpg)

2)关系

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/actor.jpg)

# references

１）http://www.openkg.cn/dataset/douban-movie-kg

２）https://www.zmonster.me/2019/04/30/neo4j-introduction.html

