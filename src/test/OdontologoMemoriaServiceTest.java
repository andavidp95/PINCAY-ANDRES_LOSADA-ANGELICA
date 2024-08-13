package test;

import dao.impl.DaoMemoriaOdontologo;
import model.Odontologo;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OdontologoService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoMemoriaServiceTest {
    static Logger logger = Logger.getLogger(OdontologoMemoriaServiceTest.class);
    @Test
    @DisplayName("Testear que un odontologo se guarde en la base de datos")
    void caso1(){
        //dado
        OdontologoService odontologoService = new OdontologoService(new DaoMemoriaOdontologo());
        Odontologo odontologo = new Odontologo(12121,"Romero","Luciana");
        // cuando
        Odontologo odontologoDesdeDB = odontologoService.guardarOdontologo(odontologo);
        // entonces
        assertNotNull(odontologoDesdeDB.getId());
    }

    @Test
    @DisplayName("Testear que se muestren todos los odontologos")
    void caso2(){
        //dado
        OdontologoService odontologoService2 = new OdontologoService(new DaoMemoriaOdontologo());
        //cuando
        odontologoService2.guardarOdontologo(new Odontologo(1, "Juan", "Perez"));
        odontologoService2.guardarOdontologo(new Odontologo(2, "Ana", "Gomez"));
        odontologoService2.guardarOdontologo(new Odontologo(2, "Ana", "Gomez"));
        List<Odontologo> odontologos = odontologoService2.buscarTodos();
        // entonces
        assertEquals(3, odontologos.size());
    }




}