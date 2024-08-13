package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Medicamento;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2Medicamento implements IDao<Medicamento> {
    private static final Logger logger = Logger.getLogger(DaoH2Medicamento.class);
    private static final String INSERT = "INSERT INTO MEDICAMENTOS VALUES (DEFAULT,?,?,?,?,?)"; //GUARDAR
    private static final String SELECT_ALL = "SELECT * FROM MEDICAMENTOS"; // BUSCAR TODOS

    @Override
    public Medicamento guardar(Medicamento medicamento) {
        Connection connection = null;
        Medicamento medicamentoARetornar = null; //El medicamento que voy a traer con el id de la base de datos
        //Para GUARDAR -> una transaccion
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            // el segundo argumento es para traer la Key generada
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, medicamento.getCodigo());
            preparedStatement.setString(2, medicamento.getNombre());
            preparedStatement.setString(3, medicamento.getLaboratorio());
            preparedStatement.setInt(4,medicamento.getCantidad());
            preparedStatement.setDouble(5,medicamento.getPrecio());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            Integer id = null;
            if (resultSet.next()){
                id=resultSet.getInt(1);
            }
            medicamentoARetornar = new Medicamento(id,medicamento.getCodigo(),
                    medicamento.getNombre(),medicamento.getLaboratorio(),
                    medicamento.getCantidad(), medicamento.getPrecio());
            logger.info("medicamento persistido "+ medicamentoARetornar);
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
        return medicamentoARetornar;
    }

    @Override
    public List<Medicamento> buscarTodos() {
        Connection connection = null;
        List<Medicamento> medicamentos = new ArrayList<>();
        Medicamento medicamento = null;
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer codigo = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String laboratorio = resultSet.getString(4);
                Integer cantidad = resultSet.getInt(5);
                double precio = resultSet.getDouble(6);
                medicamento = new Medicamento(id, codigo, nombre, laboratorio, cantidad, precio);
                logger.info(medicamento);
                medicamentos.add(medicamento);
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
        return medicamentos;
    }
}
