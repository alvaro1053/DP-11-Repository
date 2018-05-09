package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Advertisement;
import services.AdvertisementService;

@Component
@Transactional
public class StringToAdvertisementConverter implements Converter<String, Advertisement> {

	@Autowired
	AdvertisementService	advertisementService;


	@Override
	public Advertisement convert(final String text) {
		Advertisement result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.advertisementService.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
