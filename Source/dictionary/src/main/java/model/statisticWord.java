package model;

import java.io.Serializable;
import java.time.LocalDate;

public class statisticWord implements Serializable{
  private String _word;
  private LocalDate _date;
  
  public statisticWord(String _word, LocalDate _date)
  {
	  this._word = _word;
	  this._date = _date;
  }

  public String get_word() {
	return _word;
  }

  public void set_word(String _word) {
	this._word = _word;
  }

  public LocalDate get_date() {
	return _date;
  }

  public void set_date(LocalDate _date) {
	this._date = _date;
 }
}
