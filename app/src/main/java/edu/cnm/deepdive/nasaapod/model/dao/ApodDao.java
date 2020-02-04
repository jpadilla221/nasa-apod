package edu.cnm.deepdive.nasaapod.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Dao
public interface ApodDao {

  @Insert
  long insert(Apod apod);

  @Insert
  List<Long> insert(Collection<Apod> apods);

  @Insert
  List<Long> insert(Apod... apods);

  @Delete
  int delete(Apod apod);

  @Delete
  int delete(Collection<Apod> apods);

  @Delete
  int delete(Apod... apods);


  @Query("SELECT * FROM Apod ORDER BY date  DESC")
  List<Apod> select();

  @Query("SELECT * FROM Apod  WHERE date = :date")
  Apod select(Date date);

  @Query("SELECT * FROM Apod  WHERE apod_id = :id")
  Apod select(long id);
}
