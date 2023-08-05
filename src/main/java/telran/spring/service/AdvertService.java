package telran.spring.service;

import telran.spring.model.Advert;
import java.util.List;

public interface AdvertService {
	Advert addAdvert(Advert advert);

	List<Advert> getAllAdverts();

	List<Advert> getAdvertsByCategory(String category);

	List<Advert> getAdvertsByPrice(double maxPrice);

	Advert editAdvert(int id, Advert advert);

	void deleteAdvert(int id);

	List<Advert> addAdverts(List<Advert> adverts);
}
