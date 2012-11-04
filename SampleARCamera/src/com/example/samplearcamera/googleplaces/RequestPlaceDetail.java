package com.example.samplearcamera.googleplaces;

import com.example.samplearcamera.network.NetworkRequest;
import com.example.samplearcamera.util.Config;

public class RequestPlaceDetail extends NetworkRequest {

	public RequestPlaceDetail(GooglePlaceItem item) {
		this(item.reference,false);
	}
	
	public RequestPlaceDetail(GooglePlaceItem item,boolean sensor) {
		this(item.reference,sensor);
	}

	public RequestPlaceDetail(String reference) {
		this(reference, false);
	}
	
	public RequestPlaceDetail(String reference,boolean sensor) {
		String param;
		param = "key=" + Config.KEY;
		param += "&reference=" + reference;
		param += "&sensor=" + sensor;
		this.urlString = GooglePlaceConstants.PLACE_DETAILS_URL + param;
		this.parser = new GooglePlaceDetailParser();
	}
}
