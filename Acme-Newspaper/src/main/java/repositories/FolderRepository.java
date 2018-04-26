
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select f from Folder f, Actor a  where (f member of a.folders and f.name='notification box' and a.id=?1)")
	Folder findNotificationFolderActorId(int actorId);

	@Query("select f from Folder f, Actor a  where (f member of a.folders and f.name='in box' and a.id=?1)")
	Folder findInBoxFolderActorId(int actorId);

	@Query("select f from Folder f, Actor a  where (f member of a.folders and f.name='out box' and a.id=?1)")
	Folder findOutBoxFolderActorId(int actorId);

	@Query("select f from Folder f, Actor a  where (f member of a.folders and f.name='spam box' and a.id=?1)")
	Folder findSpamFolderActorId(int actorId);

	@Query("select f from Folder f, Actor a  where (f member of a.folders and f.name='trash box' and a.id=?1)")
	Folder findTrashFolderActorId(int actorId);

	@Query("select f from Folder f, Actor a where (f member of a.folders and f.parentFolder is null and a.id=?1)")
	Collection<Folder> findAllWithoutParentFolderByPrincipal(int actorId);

	@Query("select f from Folder f, Actor a where (f member of a.folders and a.id=?1)")
	Collection<Folder> findAllByPrincipal(int actorId);
}
