package edu.cnm.deepdive.nasaapod.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Dao
public interface ApodDao {

  @Insert
  Single<Long> insert(Apod apod);

  @Insert
  Single<List<Long>> insert(Collection<Apod> apods);

  @Insert
  List<Long> insert(Apod... apods);

  @Delete
  Single <Integer> delete(Apod apod);

  @Delete
  Single <Integer> delete(Collection<Apod> apods);

  @Delete
  Single <Integer> delete(Apod... apods);


  @Query("SELECT * FROM Apod ORDER BY date  DESC")
  LiveData<List<Apod>> select();

  @Query("SELECT * FROM Apod  WHERE date = :date")
  Maybe<Apod> select(Date date);

  @Query("SELECT * FROM Apod  WHERE apod_id = :id")
  Single<Apod> select(long id);
}
