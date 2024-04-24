package com.pulsar.api.components;

import com.pulsar.api.AudioManager;
import com.pulsar.api.Component;
import com.pulsar.api.audio.AudioClip;

public class StreamedAudioSource extends Component {

 public AudioClip audioClip;
 public boolean playOnStart = false;
 public boolean loop = false;

 public float volume;
 public float pitch;


 public void start() {}

 public void update() {}

 public void play() {}

 public void stop() {}

 public void pause() {}

 public float getPlaybackPosition() {return 0.0f;}

 
public float getDuration() {return 0.0f;}


}
