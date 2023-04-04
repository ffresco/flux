package ar.com.bancogalicia.pausados.model;

import java.util.List;
import lombok.Data;

@Data
public class RestPomLoginResponse{
	private List<DataItem> data;
	private Meta meta;
}