package by.epam.training.Entity;

import java.util.ArrayList;

/**
 * Created by User on 30.09.2016.
 */
public class Dish {
        private String photoLink;
        private String name;
        private String description;
        private String weight;
        private String count;
        private String price;

        public Dish(){
            this.photoLink = "";
            this.name = "";
            this.description = "";
            this.weight = "";
            this.count = "";
            this.price = "";
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

        public String getWeight() {
            return this.weight;
        }

        public void setWeight(String weight) {
            this.weight = weight ;
        }

        public String getCount() {
            return this.count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

}
