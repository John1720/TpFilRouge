package shop.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Index;
import org.hibernate.mapping.Table;
import org.hibernate.query.Query;

import shop.model.bean.Cart;
import shop.model.bean.Product;

public class CartModel {	

		private static CartModel instance = null;
		
		public static CartModel getInstance() {
			if( CartModel.instance == null ) {
				CartModel.instance = new CartModel();
			}
			return CartModel.instance;
		}
		private boolean connected;
		private Connection bdd;
		private SessionFactory fact;
		
		private CartModel(){
			super();
			
			// on n'est pas connecté par défaut
			this.connected 	= false;
			
			// on va chercher un fabricant de "registre hibernate" ( un objet de config )
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
			
			// le fabricant va utilier le fichier hibernate.cfg.xml pour construire
			// un registre.
			StandardServiceRegistry reg = builder.configure().build();
			
			// maintenant on va donner le registre à la SessionFactory
			SessionFactory fact = new MetadataSources(reg)
										.buildMetadata()
										.buildSessionFactory();
			
			this.fact = fact;
			this.connected 	= true;
		}
		
		public boolean isConnected() {
			//System.out.println(" Cart Model Is Connected ");
			return this.connected;
		}
		
		public ArrayList<Cart> getCart(){
			ArrayList<Cart> results = new ArrayList<Cart>();
			
			Session bdd = this.fact.openSession();
			bdd.beginTransaction();
			
			results = (ArrayList<Cart>) bdd.createQuery("from Cart").list();
			
			bdd.getTransaction().commit();
			bdd.close();
			return results;
		}
		
		public void add(Long p_id, int p_Product_id ) {			
			System.out.println(" Cart Model Add ");
			Session bdd = this.fact.openSession();
			bdd.beginTransaction();
			
			bdd.save( new Cart (p_id,p_Product_id) );
			
			bdd.getTransaction().commit();
			bdd.close();			
		}
		
		public boolean removeProductById( Long p_id ) {
			System.out.println(" Cart Model Remove By ID " + p_id );
			Session bdd = this.fact.openSession();
			bdd.beginTransaction();

			bdd.remove( bdd.get(Cart.class, p_id) ); 			
						
			bdd.getTransaction().commit();
			bdd.close();
			
			return true;
		}

		
	}
