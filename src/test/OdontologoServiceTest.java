package test;

import dao.impl.DaoH2Odontologo;
import model.Odontologo;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OdontologoService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {
    static final Logger logger = Logger.getLogger(OdontologoServiceTest.class);
    OdontologoService odontologoService = new OdontologoService(new DaoH2Odontologo());
    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try{
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./farmacia;INIT=RUNSCRIPT FROM 'create.sql'", "sa","sa");
        }catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Testear que un odontologo se guardo en la bd")
    void caso1(){
        //dado
        Odontologo odontologo = new Odontologo(323123,"Pepita","PEPITA");
        // cuando
        Odontologo odontologoDesdeLaDB = odontologoService.guardarOdontologo(odontologo);
        //entonces
        assertNotNull(odontologoDesdeLaDB.getId());
    }


    @Test
    @DisplayName("Testear que me traiga todos los odontologos guardados")
    void caso2(){
        //dado
        List<Odontologo> odontologos = new ArrayList<>();
        //cuando
        odontologos = odontologoService.buscarTodos();
        //entonces
        //assertFalse(odontologos.isEmpty());
        assertTrue(odontologos.size()!= 0);

    }

}