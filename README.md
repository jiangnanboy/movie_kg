# douban-movie-kg
    存储豆瓣电影知识图谱，展示导演、编剧、演员与电影的关系。利用spark和neo4j完成一个简易的电影问答程序。
    
# introduction
    利用java-driver方式，使用cypher和apoc构建节点和关系，使用spark完成问句分类。
    1.neo4j3.5.3
    2.apoc3.5.0.13
    3.jdk1.8
    4.hanlp1.7.7 下载data(包括dictionary和model，配置hanlp.properties路径)放在resources目录下即可
    5.spark3.0

# quick start
	一.cypher构建node与relation(较慢，一个一个语句create)
	    \src\main\java\com\sy\mainclass\GraphCypherBuild.java
	    1.构建节点
	    createNode(driver);
	    2.构建关系
        createRelation(driver);
        
    二.apoc批量构建node与relation(建议利用apoc构建，不需要stop neo4j，速度和数据量中等)
        \src\main\java\com\sy\mainclass\GraphApocBuild.java
        1.构建节点
        createNode(driver);
        2.构建关系
        createRelation(driver);
        
    三.\src\main\java\com\sy\mainclass\MovieQA.java(问答部分，问句意图识别和模板匹配，并转为查询语句)
        1.对问句进行意图分类和模板匹配
        2.提取问答语句中的实体，包括人名和电影名
        3.将问句模板和提取的实体转为cypher或apoc语句进行查询
    
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

关系图

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/movie_graph.png)

问答

![image](https://github.com/jiangnanboy/movie_kg/tree/master/image/reponse_result.png)

# references

１）http://www.openkg.cn/dataset/douban-movie-kg

２）https://www.zmonster.me/2019/04/30/neo4j-introduction.html

