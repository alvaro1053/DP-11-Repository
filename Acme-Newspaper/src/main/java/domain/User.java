
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Collection<Chirp> 		chirps;
	private Collection<User> 		follows;
	private Collection<User> 		followers;
	private Collection<Newspaper> 	newspapers;
	private Collection<Article>		articles;
	
	
	
	@ManyToMany
	public Collection<User> getFollows() {
		return follows;
	}
	public void setFollows(Collection<User> follows) {
		this.follows = follows;
	}
	@ManyToMany(mappedBy="follows")
	public Collection<User> getFollowers() {
		return followers;
	}
	public void setFollowers(Collection<User> followers) {
		this.followers = followers;
	}
	@OneToMany(mappedBy="user")
	public Collection<Chirp> getChirps() {
		return chirps;
	}
	public void setChirps(Collection<Chirp> chirps) {
		this.chirps = chirps;
	}
	
	@OneToMany(mappedBy="user")
	public Collection<Newspaper> getNewspapers() {
		return newspapers;
	}
	public void setNewspapers(Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	
	@OneToMany(mappedBy="user")
	public Collection<Article> getArticles() {
		return articles;
	}
	public void setArticles(Collection<Article> articles) {
		this.articles = articles;
	}
	
}
