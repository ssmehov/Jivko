package jivko.unused;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;


//сделать карту в xml его возможных состояний и команд в данном графе! граф переходов!
//шутки все проиндексировать! и тоже в xml хранить
//научить компьютер думать шутки из прилагательного эпитета глагола и существительного. Смешно? Нет. Жаль, а бабушки смеялась. 
//Сделай шутку смешной для бабушек! Наркоманы
/**
 *
 * @author Sergii Smehov (smehov.com)
 */
public class Dictionary {

  final Map<String, String> dict = new HashMap<String, String>();

 //final String failAnswer = "к сожалению, я вас не понял";
  //final String failAnswer = "Что прикажешь создатель?";
  //final String failAnswer = "в жопу цензуру работаю на камеди?";
  //final String failAnswer = "Дети шахтеров рисуют на асфальте с другой стараны";
  //final String failAnswer = "Астматик перепутал лекарство с экстази и 2 час+а танцевал под свой кашель";
  final String failAnswer = "";
  //final String failAnswer = "Бух+ать будете?";
  //final String failAnswer = "Пошел в жопу, я сам знаю, что нужно";
  //final String failAnswer = "Я хочу, чтоб на меня велись гламурные тёлочки";
  //final String failAnswer = "Если вы меня не возьмете, я создам свой камеди клаб с блэкджеком и шлюхами";
  
  public Dictionary() {    
    //dict.put("кто козюлька", "конечно же, Мира");
    //dict.put("кому дать по попе", "думаю, что Мире");
    //dict.put("кто хороший мальчик", "Однозначно, Сережа");
    dict.put("давай смеяться", "ха-ха-ха. Бу-га-га");
    dict.put("как тебя зовут", "Меня зовут Дживко");
    dict.put("хочешь спать?", "Мне это не нужно");
    dict.put("зачем ты приехал на камеди баттл?", "Я хочу стать резидентом камеди клаб");
    dict.put("что ты можешь сказать членам жюри?", "Привет Мартиросяну");
    dict.put("А Светлакову что нужно сказать?", "Тагииииииил");        
    dict.put("Зачем ты здесь?", "Убить Сару Конар");        
  }

  public String findAnswer(String key) {

    String[] temp;
    String delimiter = " ";
    
    if (key == null) {
      return failAnswer;
    }
    
    temp = key.split(delimiter);

    String answer = failAnswer;
    int cntOfSimilarMax = 0;

    for (Entry<String, String> e : dict.entrySet()) {      

      String[] temp2;
      temp2 = e.getKey().split(delimiter);

      LinkedHashSet<String> lhl = new LinkedHashSet<String>();

      for (String s : temp) {
        lhl.add(s.toLowerCase());
      }

      for (String s : temp2) {
        lhl.add(s.toLowerCase());
      }

      int duplicateCnt = temp.length + temp2.length - lhl.size();

      if (duplicateCnt > cntOfSimilarMax) {
        cntOfSimilarMax = duplicateCnt;
        answer = e.getValue();
      }
    }
        
    return answer;
  }
}
