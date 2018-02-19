package com.iot.spring.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;

public class NaverMsg {
	private Message message;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NaverMsg [message=" + message + "]";
	}
	
	@JsonIgnoreProperties(ignoreUnknown =true)
	public class Message{
		private Result result;

		public Result getResult() {
			return result;
		}

		public void setResult(Result result) {
			this.result = result;
		}
		@JsonIgnoreProperties(ignoreUnknown =true)
		public class Result{
			private String translatedText;

			public String getTranslatedText() {
				return translatedText;
			}

			public void setTranslatedText(String translatedText) {
				this.translatedText = translatedText;
			}
			
		}
	}
	
}
