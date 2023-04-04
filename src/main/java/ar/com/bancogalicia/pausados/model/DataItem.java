package ar.com.bancogalicia.pausados.model;

import lombok.Data;

@Data
public class DataItem{
	private String accessToken;
	private String pomOwnerId;
	private int refreshExpiresIn;
	private String scope;
	private String pomUserId;
	private String tokenType;
	private int notBeforePolicy;
	private String sessionState;
	private int expiresIn;
}