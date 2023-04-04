package ar.com.bancogalicia.pausados.model;

import lombok.Data;

@Data
public class RestPomLoginRequest{
	private ClientData clientData;
	private String grantType;
	private String clientId;
}