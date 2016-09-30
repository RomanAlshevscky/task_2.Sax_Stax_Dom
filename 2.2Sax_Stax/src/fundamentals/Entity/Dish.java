package fundamentals.Entity;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by User on 30.09.2016.
 */
public class Dish {
        private String photoLink;
        private String name;
        private String description;
        private ArrayList<String> weight;
        private String count;
        private ArrayList<String> price;

        public Dish(){
            this.photoLink = "";
            this.name = "";
            this.description = "";
            this.weight = new ArrayList<>();
            this.count = "";
            this.price = new ArrayList<>();
        }

        public String getPhoto() {
            return this.photoLink;
        }

        public void setPhoto(String link) {
            this.photoLink = link;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ArrayList<String> getWeight() {
            return this.weight;
        }

        public void addWeight(String weight) {
            this.weight.add(weight) ;
        }

        public String getCount() {
            return this.count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public ArrayList<String> getPrice() {
            return this.price;
        }

        public void addPrice(String price) {
            this.price.add(price);
        }

}
