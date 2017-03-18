package entities;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
 
public class SpeechUtils {
 
 private static final String VOICENAME_kevin = "kevin";
 private String text; // string to speech
 
 public SpeechUtils(String text) {
  this.text = text;
 }
 
 public void speak() {
  Voice voice;
  VoiceManager voiceManager = VoiceManager.getInstance();
  voice = voiceManager.getVoice(VOICENAME_kevin);
  voice.allocate();
  voice.speak(text);
 }
}
/*  String text = "FreeTTS was written by the Sun Microsystems Laboratories "
    + "Speech Team and is based on CMU's Flite engine.";
  FreeTTS freeTTS = new FreeTTS(text);
  freeTTS.speak();
 }*/
