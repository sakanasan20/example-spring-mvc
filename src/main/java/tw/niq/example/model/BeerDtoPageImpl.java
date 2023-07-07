package tw.niq.example.model;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class BeerDtoPageImpl extends PageImpl<tw.niq.example.model.BeerDto> {

	private static final long serialVersionUID = -5234398439453620672L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public BeerDtoPageImpl(@JsonProperty("content") List<tw.niq.example.model.BeerDto> content, 
			@JsonProperty("page") int page, 
			@JsonProperty("size") int size, 
			@JsonProperty("total") long total) {
		super(content, PageRequest.of(page, size), total);
	}

	public BeerDtoPageImpl(List<tw.niq.example.model.BeerDto> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public BeerDtoPageImpl(List<tw.niq.example.model.BeerDto> content) {
		super(content);
	}

}
