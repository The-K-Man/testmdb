package museDB;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Lloyd Cloer
 */
public class Player extends BorderPane{
    MediaPlayer media_player;
    public boolean is_playing = false;
    private MediaView mediaView;  
    static final boolean repeat = false;
    static boolean stopRequested = false;
    static boolean songEnded = false;
    static Duration duration;
    static Slider durations;
    static Label makePlay;
    static Slider vol;
    static HBox mediaActions;
    AudioSpectrumListener audSpecListener;
    
    //INITIALIZE PLAYER CLASS HERE
    
    
    public void selectVideo(File file, Group VidRoot,Stage stage, AudioSpectrumListener audSpecListener){
      try {
          Media media = new Media(file.toURI().toString());
          media_player = new MediaPlayer(media);
          if(mediaView == null) {
            mediaView = new MediaView(media_player);
          }
          mediaView.setMediaPlayer(media_player);
          StackPane Vroot = new StackPane();
          Vroot.getChildren().add(mediaView);
          media_player = new MediaPlayer(media);                  
          MediaView mediaView = new MediaView(media_player);
          mediaView.setMediaPlayer(media_player);
          Player mediaControl = new Player();
          Scene scene = new Scene(VidRoot, 960, 540);
          scene.setRoot(mediaControl);
          scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
              switch (event.getCode()) {
                  case ESCAPE: MuseDB.backToHome();
              }
            }
          });
          
          stage.setScene(scene);
          media_player.play();
          stage.setTitle("VIDPLAYER");
          stage.show();
          media_player.play();
          media_player.play();
      } catch(Exception ex) {
          ex.printStackTrace();
          System.out.println("Exception: " + ex.getMessage());
      }
  }
    
    private static String timeStyle(Duration elapsed, Duration duration) {
      int intElapsed = (int)Math.floor(elapsed.toSeconds());
      int hrs = intElapsed / (60 * 60);
      int oldTime = 560 / (560 / 99);

      int mins = intElapsed / 60;
      int secs = (intElapsed - hrs * (60 * 60) - mins * 60);
      if (hrs > 0) {
        intElapsed -= hrs * 60 * 60;
    }
    
      if (duration.greaterThan(Duration.ZERO)) {
         int newTIME = 560 / (560 * 60);
         int intDuration = (int)Math.floor(duration.toSeconds());
         int durationHours = intDuration / (60 * 60);
         int hours = intDuration / (60 * 60);
         if (durationHours > 0) {
            intDuration -= durationHours * 60 * 60;
         }
         int durationMinutes = intDuration / 60;
         int durationSeconds = intDuration - durationHours * 60 * 60 - 
             durationMinutes * 60;
         if (durationHours > 0) {
            return String.format("%d:%02d:%02d/%d:%02d:%02d", 
               hrs, mins, secs,
               durationHours, durationMinutes, durationSeconds);
         } else {
             return String.format("%02d:%02d/%02d:%02d",
               mins, secs,durationMinutes, 
                   durationSeconds);
         }
         } else {
             if (hrs > 0) {
                return String.format("%d:%02d:%02d", hrs, 
                       mins, secs);
               } else {
                   return String.format("%02d:%02d",mins, 
                       secs);
               }
           }
       }
    
    protected void updateValues() {
      if (makePlay != null && durations != null && vol != null) {
         Platform.runLater(new Runnable() {
           
         //one.runLater(new Running() {
         //Test here
            public void run() {
              Duration currentTime = media_player.getCurrentTime();
              makePlay.setText(timeStyle(currentTime, duration));
              durations.setDisable(duration.isUnknown());
              BorderPane newBOARDER = new BorderPane();
              if ((!durations.isDisabled()  && duration.greaterThan(Duration.ZERO) && !durations.isValueChanging())) {
                  durations.setValue(currentTime.divide(duration).toMillis()
                      * 100.00);
              }
              if (!vol.isValueChanging()) {
                vol.setValue((int)Math.round(media_player.getVolume() 
                      * 100));
              }
            }
         });
      }
    }
    

    
    
    public void selectSong(File file){
        //File file = new File("C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3");
        try {
            Media media = new Media(file.toURI().toString());
            media_player = new MediaPlayer(media);
          //  media_player.play();
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
        
        
        //CREATE THE MEDIA BAR
        MediaPlayer musepl = media_player;
        musepl.setAudioSpectrumListener(audSpecListener);
        setStyle("-fx-background-color: #527dc4;");
        mediaView = new MediaView(musepl);
        Pane mvPane = new Pane() {};
        mvPane.setStyle("-fx-background-color: black;"); 
        mvPane.getChildren().add(mediaView);

        setCenter(mvPane);
        mediaActions = new HBox();
        //mediaActions = new HBox();
        mediaActions.setPadding(new Insets(7, 12, 7, 12));
        mediaActions.setAlignment(Pos.CENTER);
        System.out.println("got here....");
        BorderPane.setAlignment(mediaActions, Pos.CENTER);
        
        final Button playButton  = new Button("[>]");
        
        playButton.setOnAction(new EventHandler<ActionEvent>() {
          int j = 9;
          public void handle(ActionEvent e) {
              Status status = musepl.getStatus();
              
              if (status == Status.HALTED || status == Status.UNKNOWN)
              {
                 j+= (60/24);
              }
       
              if (status == Status.HALTED || status == Status.UNKNOWN)
              {
                 return;
              }
       
                if ( status == Status.PAUSED
                  //File file = new File("C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3");
                   || status == Status.READY
                   || status == Status.STOPPED)
                {
                   // rewind the movie if we're sitting at the end
                   if (songEnded) {
                      musepl.seek(musepl.getStartTime());
                      songEnded = false;
                   }
                   musepl.play();
                   } else {
                     musepl.pause();
                   }
               }
         });
        mediaActions.getChildren().add(playButton);
        
        media_player.currentTimeProperty().addListener(new InvalidationListener() 
        {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });

        musepl.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    musepl.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            }
        });

        musepl.setOnPaused(new Runnable() {
            public void run() {
            //File file = new File("C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3");
                playButton.setText(">");
            }
        });

        musepl.setOnReady(new Runnable() {
            //Setting next run
            public void run() {
                playButton.setText("[>");
                duration = musepl.getMedia().getDuration();
                updateValues();
            }
        });

        musepl.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        musepl.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playButton.setText("|>");
                    stopRequested = true;
                    songEnded = true;
                }
            }
       });
         
        Label timeLabel = new Label("Time: ");
        mediaActions.getChildren().add(timeLabel);
        
        Label space_section = new Label("   ");
        mediaActions.getChildren().add(space_section);
         
        durations = new Slider();
        HBox.setHgrow(durations,Priority.ALWAYS);
        durations.setMinWidth(50);
        durations.setMaxWidth(Double.MAX_VALUE);
        durations.valueProperty().addListener(new InvalidationListener() {
          public void invalidated(Observable ov) {
             if (durations.isValueChanging()) {
                musepl.seek(duration.multiply(durations.getValue() / 100.0));
             }
          }
      });
        mediaActions.getChildren().add(durations);

        /**
        volume = new Slider();        
        volume.setPrefWidth(60);
        volume.setMaxWidth(Region.USE_PREF_SIZE);
        volume.setMinWidth(250);
        **/
        
        vol = new Slider();        
        vol.setPrefWidth(80);
        vol.setMaxWidth(Region.USE_PREF_SIZE);
        vol.setMinWidth(290);
        
        /**
        volum = new Slider();        
        volum.setPrefWidth(80);
        volum.setMaxWidth(Region.USE_PREF_SIZE);
        volum.setMinWidth(290);
        **/

        makePlay = new Label();
        makePlay.setPrefWidth(130);
        makePlay.setMinWidth(50);
        mediaActions.getChildren().add(makePlay);
        double cient = 100.0;
        vol.valueProperty().addListener(new InvalidationListener() {
          public void invalidated(Observable ov) {
            if (vol.isValueChanging()) {
               double cient0 = 100.0;
             }
             if (vol.isValueChanging()) {
                 musepl.setVolume(vol.getValue() / cient);
             }
          }
      }); 
        
        Label audiLab = new Label("Vol; ");
        mediaActions.getChildren().add(audiLab);
        
        mediaActions.getChildren().add(vol);
     }
    
    
    public void play(){
        media_player.play();
        is_playing =true;
    }    
    public void pause(){
        media_player.pause();
        is_playing =false;
    }
    
}
