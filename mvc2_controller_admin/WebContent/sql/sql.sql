create table member(
  memberseq NUMBER PRIMARY key,
  id varchar(15),
  pass varchar(15),
  name varchar(15),
  phone number,
  address varchar(50),
  ROLE varchar(20) DEFAULT 'member'
);

create table ordertable(
  orderseq number primary key,
  memberseq number,
  orderproductcount number default 0,
  constraint fk_ordertable_memberseq  foreign key(memberseq) references member(memberseq) 
);

create table producttable(
  productseq number primary key,
  name varchar(30),
  content varchar(400),
  opcount number default 0,
  productcount number default 0, 
  price number default 0
);

create table product_option(
  optionseq number primary key,
  productseq number,
  name varchar(20),
  productcount number,
  productprice number,
 constraint fk_product_option_productseq  foreign key(productseq) references producttable(productseq)
);


create table order_producttable(
  opseq number primary key,
  orderseq number,
  productseq number,
  optionseq number,
  ordercount number,
  price number,
  deliveryStatus number,
  constraint fk_order_product_orderseq  foreign key(orderseq) references ordertable(orderseq), 
  constraint fk_order_product_productseq  foreign key(productseq) references producttable(productseq),
  constraint fk_order_product_optionseq  foreign key(optionseq) references product_option(optionseq)
);

create table basket(
  basketseq number primary key,
  productseq number,
  optionseq number,
  ordercount number,
  memberseq NUMBER,
  constraint fk_basket_productseq  foreign key(productseq) references producttable(productseq), 
  constraint fk_basket_optionseq  foreign key(optionseq) references product_option(optionseq),
  constraint fk_basket_memberseq  foreign key(memberseq) references member(memberseq)
);

create table product_image(
 imageseq number primary key,
 productseq number,
 name varchar(30),
 path varchar(100),
 space varchar (20),
 constraint fk_product_image_productseq  foreign key(productseq) references producttable(productseq)
);