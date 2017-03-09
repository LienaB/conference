package ua.rd.cm.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rd.cm.domain.Topic;
import ua.rd.cm.dto.CreateTopicDto;
import ua.rd.cm.repository.TopicRepository;
import ua.rd.cm.repository.specification.topic.TopicById;
import ua.rd.cm.repository.specification.topic.TopicByName;
import ua.rd.cm.services.TopicService;
import ua.rd.cm.services.exception.TopicNotFoundException;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final ModelMapper modelMapper;
    private TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(ModelMapper modelMapper, TopicRepository topicRepository) {
        this.modelMapper = modelMapper;
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic find(Long id) {
        List<Topic> topics = topicRepository.findBySpecification(new TopicById(id));
        if (topics.isEmpty()) {
            throw new TopicNotFoundException();
        }
        return topics.get(0);
    }

    @Override
    @Transactional
    public void save(CreateTopicDto topic) {
        topicRepository.save(modelMapper.map(topic, Topic.class));
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getByName(String name) {
        List<Topic> list = topicRepository.findBySpecification(new TopicByName(name));
        if (list.isEmpty()) {
            throw new TopicNotFoundException();
        }
        return list.get(0);
    }
}
