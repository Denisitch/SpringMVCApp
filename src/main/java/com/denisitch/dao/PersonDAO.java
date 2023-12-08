package com.denisitch.dao;

import com.denisitch.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query(
                "SELECT * FROM Person",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public Person show(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM Person WHERE id=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        id)
                .stream().findAny().orElse(null);
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query(
                        "SELECT * FROM Person WHERE email=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        email)
                .stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO Person(name, age, email, address) VALUES(?, ?, ?, ?)",
                person.getName(),
                person.getAge(),
                person.getEmail(),
                person.getAddress()
        );
    }

    public void update(int id, Person updatePerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=? WHERE id=?",
                updatePerson.getName(),
                updatePerson.getAge(),
                updatePerson.getEmail(),
                updatePerson.getAddress(),
                id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM Person WHERE id=?",
                id
        );
    }

    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for (Person person : people) {
            jdbcTemplate.update(
                    "INSERT INTO Person VALUES(?, ?, ?, ?, ?)",
                    person.getId(),
                    person.getName(),
                    person.getAge(),
                    person.getEmail(),
                    person.getAddress()
            );
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Bob" + i, 25,
                    "test" + i + "@gmail.com", "Russia, Moscow, 101010"));
        }
        return people;
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate(
                "INSERT INTO Person VALUES(?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());
                        ps.setString(5, people.get(i).getAddress());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                }
        );

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }
}

