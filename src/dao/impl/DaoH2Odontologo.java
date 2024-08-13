package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2Odontologo implements IDao<Odontologo> {
    private static final Logger logger = Logger.getLogger(DaoH2Odontologo.class);
    private static final String INSERT = "INSERT INTO ODONTOLOGOS VALUES (DEFAULT,?,?,?)"; //GUARDAR
    private static final String SELECT_ALL = "SELECT * FROM ODONTOLOGOS"; // BUSCAR TODOS

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoARetornar = null; //El odontologo que voy a traer con el id de la base de datos
        //Para GUARDAR -> una transaccion
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            // el segundo argumento es para traer la Key generada
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,odontologo.getNroMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            Integer id = null;
            if (resultSet.next()){
                id=resultSet.getInt(1);
            }
            odontologoARetornar = new Odontologo(id,odontologo.getNroMatricula(), odontologo.getNombre(), odontologo.getApellido());
            logger.info("odontologo persistido "+ odontologoARetornar);
        } catch (Exception e ) {
            logger.error(e.getMessage());
            try {
                connection.rollback(); //para la transaccion
            } catch (SQLException ex) {
                logger.error(e.getMessage());
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(e.getMessage());
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return odontologoARetornar;
    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();
        Odontologo odontologo = null;
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer nroMatricula = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String apellido = resultSet.getString(4);
                odontologo = new Odontologo(id, nroMatricula, nombre, apellido);
                logger.info(odontologo);
                odontologos.add(odontologo);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
        return odontologos;
    }
}
