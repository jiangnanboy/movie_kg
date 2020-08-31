package com.sy.manipulation.cypher.csv;

import com.sy.base.abs.AbstractCSVRead;
import com.sy.node.Country;
import com.sy.node.Movie;
import com.sy.node.Person;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;

import static org.neo4j.driver.v1.Values.parameters;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


/**
 * Created by YanShi on 2020/7/24 9:22 上午
 */
public class CreateNode extends AbstractCSVRead {

    public CreateNode(Driver drive) {
        super(drive);
    }

    /**
     * 创建 person node
     * @param csvFile
     */
    public void createPersonNode(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath)); Session session = driver.session(); ) {//Transaction tx = session.beginTransaction()
            Iterator<Person> iterator = readCSV(br, Person.class);
            while (iterator.hasNext()) {
                Person person = iterator.next();
                session.writeTransaction(tx -> tx.run("create (p:Person {name:{name}, id:{id}})", parameters("name", person.name, "id", person.id)));
            }
            session.writeTransaction(tx -> tx.run("create index on :Person(id)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create person node done!");
    }

    /**
     * 创建 country node
     * @param csvFile
     */
    public void createCountryNode(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session(); ) {//Transaction tx = session.beginTransaction()
            Iterator<Country> iterator = readCSV(br, Country.class);
            while (iterator.hasNext()) {
                Country country = iterator.next();
                session.writeTransaction(tx -> tx.run("create (c:Country {name:{name}, id:{id}})", parameters("name", country.name, "id", country.id)));
            }
            session.writeTransaction(tx -> tx.run("create index on :Country(id)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create country node done!");
    }

    /**
     * 创建 movie node
     * @param csvFile
     */
    public void createMovieNode(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session(); ) {//Transaction tx = session.beginTransaction()
            Iterator<Movie> iterator = readCSV(br, Movie.class);
            while (iterator.hasNext()) {
                Movie movie = iterator.next();
                session.writeTransaction(tx -> tx.run("create (m:Movie {title:{title}, url:{url}, cover:{cover}, rate:{rate}, category:{category}, language:{language}, showtime:{showtime}, length:{length}, othername:{othername}, id:{id}})",
                        parameters("title", movie.title, "url", movie.url, "cover", movie.cover, "rate", movie.rate, "category", movie.category, "language", movie.language, "showtime", movie.showtime, "length", movie.length, "othername", movie.othername, "id", movie.id)));
            }
            session.writeTransaction(tx -> tx.run("create index on :Movie(id)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create movie node done!");
    }

}
