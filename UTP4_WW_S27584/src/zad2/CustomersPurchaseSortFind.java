/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CustomersPurchaseSortFind {

    private final List<Purchase> purchases = new ArrayList<>();

    public void readFile(String fname){
        try{
            Scanner scanner = new Scanner(new File(fname));

            while (scanner.hasNextLine()){
                String[] split = scanner.nextLine().split(";");
                Purchase purchase = new Purchase(split[0], split[1], split[2], Double.parseDouble(split[3]), Double.parseDouble(split[4]));
                this.purchases.add(purchase);
            }

            scanner.close();
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void showSortedBy(String sortBy){
        System.out.println(sortBy);

        List<Purchase> purchasesToSort = new ArrayList<>(this.purchases);

        if (sortBy.equals("Nazwiska")){
            Collections.sort(purchasesToSort, (o1, o2) -> {
                if (!o1.getSurnameName().equals(o2.getSurnameName())) {
                    return o1.getSurnameName().compareTo(o2.getSurnameName());
                }else{
                    return o1.getId().compareTo(o2.getId());
                }
            });

            for (Purchase p : purchasesToSort){
                System.out.println(p);            }
        }else if (sortBy.equals("Koszty")){
            Collections.sort(purchasesToSort, (o2, o1) -> {
                if ((o1.getCost()*o1.getAmount()) != (o2.getCost()* o2.getAmount())){
                    return Double.compare(o1.getCost()*o1.getAmount(), o2.getCost()* o2.getAmount());
                }else{
                    return o2.getId().compareTo(o1.getId());
                }
            });

            for (Purchase p : purchasesToSort){
                System.out.println(p + " (koszt: " + p.getCost()*p.getAmount() + ")");            }
        }

        System.out.println();
    }

    public void showPurchaseFor(String id){
        System.out.println("Klient " + id);

        for (Purchase p : this.purchases){
            if (p.getId().equals(id)){
                System.out.println(p);
            }
        }

        System.out.println();
    }
}
