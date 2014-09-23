package services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.system.SystemObjectImpl;

import org.eclipse.jetty.util.ByteArrayOutputStream2;
import org.omg.IOP.Encoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Objects.Course;
import Objects.Recording;

@Service
public class AudioService extends SystemObjectImpl {

	// *******************************************************************
	// In order for this service to work you must install VB audio virtual cable
	// located here: V:\Automation\3rd party tools\VBCABLEDriver_Pack42b.zip
	// *******************************************************************
	private final int BUFFER_SIZE = 128000;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;

	@Autowired
	DbService dbService;

	@Autowired
	TextService textService;

	long recoredTime = 6000;
	List<Recording> recordings;

	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	TargetDataLine targetline;
	
//	public void sendSoundToVirtualMic(File soundFile, float sampleRate){
//		
//	}

	public void sendSoundToVirtualMic(File soundFile, float sampleRate)
			throws Exception {
		try {
			audioStream = AudioSystem.getAudioInputStream(soundFile);

		} catch (Exception e) {
			e.printStackTrace();

		}
		report.startLevel("Starting to play file: " + soundFile.getPath()
				+ " to virtual microphone", EnumReportLevel.CurrentPlace);
		System.out.println("Playing file: "+soundFile.getPath().toString());
		// audioFormat = audioStream.getFormat();
		if (sampleRate == 0) {
			audioFormat = getAudioFormat();
		} else {
			audioFormat = getAudioFormat(sampleRate);
		}
		System.out.println("Playing the file with sample rate of: "+audioFormat.getSampleRate());

		DataLine.Info infoIn = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		try {
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			Mixer mixer = null;
			for (int i = 0; i < mixerInfos.length; i++) {
				System.out.println(mixerInfos[i].getName());
				if (mixerInfos[i].getName().equals(
						"CABLE Input (VB-Audio Virtual Cable)")) {
					mixer = AudioSystem.getMixer(mixerInfos[i]);
					break;
				}
			}
			sourceLine = (SourceDataLine) mixer.getLine(infoIn);
			sourceLine.open(audioFormat);
			System.out.println("Started playing the file."+System.currentTimeMillis());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			// System.exit(1);
		}
		sourceLine.start();
		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audioStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				@SuppressWarnings("unused")
				int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
			}
		}
		sourceLine.drain();
		sourceLine.close();
		System.out.println("finished playing the file."+System.currentTimeMillis());
		report.stopLevel();

	}

	public void recoredSound() {

	}

	private AudioFormat getAudioFormat() {
		return getAudioFormat(44100.0F);
	}

	private AudioFormat getAudioFormat(float sampleRate) {
		// float sampleRate = 44100.0F;
		// 8000,11025,16000,22050,44100
		//8000 for recordings with SRI tool!
		int sampleSizeInBits = 16;
		//16 for my recordings!!!!
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		//true for my recordings
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);

		// return new
		// AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED,
		// sampleRate, sampleSizeInBits, channels, fr, f, bigEndian)
	}// end getAudioFormat

}
