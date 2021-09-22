package pl.redhat.samples.eventdriven.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.order.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.order.message.OrderCommand;
import pl.redhat.samples.eventdriven.order.message.RollbackCommand;
import pl.redhat.samples.eventdriven.order.repository.OrderCommandRepository;

@Service
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private OrderCommandRepository orderCommandRepository;
    private StreamBridge streamBridge;

    public OrderService(OrderCommandRepository orderCommandRepository, StreamBridge streamBridge) {
        this.orderCommandRepository = orderCommandRepository;
        this.streamBridge = streamBridge;
    }

    public void addOrderCommand(OrderCommand orderCommand) {
        orderCommand.setStatus("NEW");
        orderCommandRepository.save(orderCommand);
        LOG.info("New OrderCommand saved: {}", orderCommand);
    }

    public void updateOrderCommandStatus(String id) {
        OrderCommand orderCommand = orderCommandRepository.findById(id).orElseThrow();
        if (orderCommand.getStatus().equals("NEW")) {
            orderCommand.setStatus("PARTIALLY_CONFIRMED");
            orderCommandRepository.save(orderCommand);
            LOG.info("New ConfirmCommand saved: {}", orderCommand);
        } else if (orderCommand.getStatus().equals("PARTIALLY_CONFIRMED")) {
            ConfirmCommand confirmCommand = new ConfirmCommand();
            confirmCommand.setOrderId(id);
            confirmCommand.setAmount(orderCommand.getAmount());
            confirmCommand.setProductCount(orderCommand.getProductCount());
            confirmCommand.setProductId(orderCommand.getProductId());
            confirmCommand.setCustomerId(orderCommand.getCustomerId());
            streamBridge.send("confirmations-out-0", confirmCommand);
            orderCommandRepository.deleteById(id);
            LOG.info("Order confirmed: id={}", id);
        }
    }

    public void rollbackOrder(String id, String source) {
        OrderCommand orderCommand = orderCommandRepository.findById(id).orElseThrow();
        RollbackCommand rollbackCommand = new RollbackCommand();
        rollbackCommand.setOrderId(id);
        rollbackCommand.setAmount(orderCommand.getAmount());
        rollbackCommand.setProductCount(orderCommand.getProductCount());
        rollbackCommand.setProductId(orderCommand.getProductId());
        rollbackCommand.setCustomerId(orderCommand.getCustomerId());
        rollbackCommand.setSource(source);
        streamBridge.send("confirmations-out-0", rollbackCommand);
        orderCommandRepository.deleteById(id);
    }


}
