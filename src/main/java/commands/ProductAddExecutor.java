package commands;

import interfaces.IRepository;
import model.Order;
import model.OrderStatuses;
import model.TelegramMessage;
import model.User;
import repository.OrderRepository;

import java.util.List;

public class ProductAddExecutor extends CommandExecutor {
    private static final String ADD_COMMAND = "/add";
    private IRepository<Order> orderRepository = new OrderRepository();

    @Override
    public List<TelegramMessage> execute(String command, Long chatId) {
        if(command.contains(ADD_COMMAND)){
            long id = Integer.parseInt(command.split("_")[1]);
            User user = userRepository.get(chatId);

            Order order;

            var first = orderRepository.getAll().stream()
                    .filter(o -> o.getUserId() == chatId && o.getProductId() == id && o.getStatusId() == OrderStatuses.Waiting.getStatus())
                    .findFirst();

            if(first.isPresent()){
                order = first.get();
                order.setAmount(order.getAmount() + 1);

                orderRepository.update(order);
            }
            else {
                order = new Order();
                order.setUserId(chatId);
                order.setProductId(id);

                orderRepository.add(order);
            }

            if(user.getStatus() == 5){
                return new KitchenExecutor().execute("/kitchen", chatId);
            }
            else{
                return new BarExecutor().execute("/bar", chatId);
            }
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
