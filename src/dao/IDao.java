package dao;

import java.util.List;

// Usamos datos genericos ya que asi podemos reutilizar el codigo
public interface IDao <T>{
    T guardar(T t);

    List<T> buscarTodos();
}
