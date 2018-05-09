start transaction;

	
	use `acme-newspaper`;

	revoke all privileges on `acme-newspaper`.* from 'acme-user'@'%';

	revoke all privileges on `acme-newspaper`.* from 'acme-manager'@'%';
	
	drop user 'acme-user'@'%';

	drop user 'acme-manager'@'%';

	drop database `acme-newspaper`;

	commit;